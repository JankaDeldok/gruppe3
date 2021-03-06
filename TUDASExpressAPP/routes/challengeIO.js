var express = require('express');
var challengeIO = express.Router();

var multer = require("multer");
var fs = require('fs');


const User = require('../models/userSchema');
const Challenge = require('../models/challengeSchema');


// Storage for saving pictures on the server.
var storageProofImg = multer.diskStorage({
    destination: './public/images/challenges',
    filename: function (req, file, cb) {
        cb(null, file.originalname);
    }
});

var proofImg = multer({ storage: storageProofImg });

// needed for configuring cors policies
challengeIO.use(function (req, res, next) {
    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:3000');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow

    res.setHeader('Access-Control-Allow-Headers', 'Accept, Access, Control, Allow-Headers');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    next();
});


/* POST /challenge/uploadpicture */
/* adds a proof picture to a challenge and returns the updated challenge with the picture link */
challengeIO.post('/uploadpicture', proofImg.single("file"), async (req, res) => {
    console.log("Receivied picture ?")
    const { challengeName, userName } = req.body;
    let url = "http://localhost:3030/images/challenges/" + req.file.originalname;
    Challenge.findOne({ name: challengeName }).then(challenge => {
        if (challenge.dueDate == null || challenge.dueDate >= Date.now()) {
            Challenge.findOneAndUpdate({ name: challengeName },
                { $push: { proofMedia: { user: userName, location: url } } },
                { new: true })
                .then(challenge => finishedChallenge(challenge, userName))
                .then(user => res.status(200).json(user))
        }
    }).catch(err => res.status(409).send("Could not find Challenge"));
});

/* POST /challenge/uploadsocialmedia */
/* adds a proof picture to a challenge and returns the updated challenge with the picture link */
challengeIO.post('/uploadsocialmedia', async (req, res) => {
    const { challengeName, userName, url } = req.body;
    Challenge.findOne({ name: challengeName }).then(challenge => {
        if (challenge.dueDate === null || challenge.dueDate >= Date.now()) {
            Challenge.findOneAndUpdate({ name: challengeName },
                { $push: { proofMedia: { user: userName, location: url } } },
                { new: true })
                .then(challenge => finishedChallenge(challenge, userName))
                .then(user => res.status(200).json(user))
        }
    }).catch(err => res.status(409).send("Could not find Challenge"));
});

/* adds a challenge to a users finished challenges */
const finishedChallenge = async (challenge, userName) => {
    await User.findOneAndUpdate({ name: challenge.creator }, { $push: { feed: { message: userName + " finished your challenge " + challenge.name, new: true } }, $inc: { points: challenge.worth / 4 } })
    return User.findOneAndUpdate({ name: userName },
        { $push: { finishedChallenges: challenge._id }, $pull: { openChallenges: challenge._id }, $inc: { points: challenge.worth } },
        { new: true })
}

/* POST /challenge/addchallenge */
/* adds a challenge to a user, a users friendsgroup or to the public */
challengeIO.post('/addchallenge', async (req, res) => {
    let { challengeName, creatorName, description, dueDate, reward, addressedTo, worth } = req.body;

    let creator = await User.findOne({ name: creatorName })

    const newChallenge = {
        name: challengeName,
        description: description,
        creator: creator.name,
        creationDate: Date.now(),
        dueDate: dueDate,
        reward: reward,
        addressedTo: addressedTo,
        worth: worth
    }

    Challenge.create(newChallenge, (err, item) => {
        if (err) {
            console.log(err)
            res.status(500).send("Challenge could not be created.");
        } else {
            item.save(async (err, createdChallenge) => {
                await creator.updateOne({ $push: { createdChallenges: createdChallenge._id } }, { new: true });
                if (addressedTo === "public") {
                    console.log("Public challenge succesfully created")
                    res.status(200).send("Public challenge succesfully created")
                } else if (addressedTo === "friends") {
                    friendChallenge(createdChallenge._id, creatorName).then(err => {
                        if (err) {
                            res.status(409).send("Challenge could not be created!")
                        } else {
                            console.log("Challenge for your friends successfully created!")
                            res.status(200).send("Challenge for your friends successfully created!")
                        }
                    })
                } else {
                    userChallenge(createdChallenge._id, addressedTo, creatorName).then(err => {
                        if (err) {
                            res.status(409).send("Challenge could not be created!")
                        } else {
                            console.log(err)
                            console.log("Challenge for " + addressedTo + " successfully created!")
                            res.status(200).send("Challenge for " + addressedTo + " successfully created!")
                        }
                    })
                }
            })
        }
    })
});

/* adds a challenge to a users finished challenges */
const friendChallenge = async (challengeId, userName) => {
    User.findOne({ name: userName }, { friends: 1, _id: 0 })
        .then(async (friends) => {
            let friendNames = [];
            friends.friends.forEach(element => {
                friendNames.push(element.name);
            });
            await User.updateMany({ name: { $in: friendNames } },
                { $push: { openChallenges: challengeId }, $push: { feed: { message: userName + " sent you a challenge", new: true } } },
                { multi: true });
        })

}

// /* adds a challenge to a users finished challenges */
// const userChallenge = async (challengeId, friendName, userName) => {
//     await User.findOneAndUpdate({ name: friendName },
//         { $push: { openChallenges: challengeId }, $push: { feed: { message: userName + " sent you a challenge", new: true } } })
// }

/* adds a challenge to a users finished challenges */
const userChallenge = async (challengeId, friendName, userName) => {
    await User.findOneAndUpdate({ name: friendName },
        { $push: { openChallenges: challengeId, feed: { message: userName + " sent you a challenge", new: true } } })
}

/* GET /challenge/getchallenge */
/* get challenge by name */
challengeIO.post('/getchallenge', (req, res) => {
    Challenge.findOne({ name: req.body.challengeName }).then(challenge => res.status(200).json(challenge));
});

/* GET /challenge/getpublicchallenges */
/* get challenges which are public */
challengeIO.get('/getpublicchallenges', (req, res) => {
    Challenge.find({ addressedTo: "public" }).then(challenges => res.status(200).json(challenges));
});

/* GET /challenge/getchallengesfromfriends */
/* get challenges which are from friends */
challengeIO.post('/getchallengesfromfriends', (req, res) => {
    let { userName } = req.body;
    User.findOne({ name: userName }, { friends: 1, _id: 0 })
        .then(async (friends) => {
            let friendNames = [];
            friends.friends.forEach(element => {
                friendNames.push(element.name);
            });
            Challenge.find({ addressedTo: "friends", creator: { $in: friendNames } })
                .then(challenges => res.status(200).json(challenges));
        })
});

module.exports = challengeIO;