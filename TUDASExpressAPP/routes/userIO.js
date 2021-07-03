var express = require('express');
var userIO = express.Router();

var multer = require("multer");
var fs = require('fs');


const User = require('../models/userSchema');
const Challenge = require('../models/challengeSchema');


// Storage for saving pictures on the server.
var storageProfileImg = multer.diskStorage({
    destination: './public/images/profiles',
    filename: function (req, file, cb) {
        cb(null, file.originalname);
    }
});

var profileImg = multer({ storage: storageProfileImg });

// needed for configuring cors policies
userIO.use(function (req, res, next) {
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


/* POST /user/uploadpicture */
/* adds a profile picture to a user and returns the updated user with the picture link */
userIO.post('/uploadpicture', profileImg.single("file"), async (req, res) => {
    let { userName } = req.body;
    let url = "http://localhost:3030/images/profiles/" + req.file.originalname;
    User.findOneAndUpdate({ name: userName },
        { profilepicture: url },
        { fields: { password: 0 }, new: true })
        .then(user => res.status(200).json(user));
});

/* POST /user/addfriend */
/* adds a friend to a user if they are not already friends */
userIO.post('/addfriend', async (req, res) => {
    let { userName, friendName } = req.body;
    User.findOne({ name: userName }, { friends: 1 }).then(user => {
        let alreadyFriends = false;
        user.friends.some(element => {
            if (element.name === friendName) {
                return alreadyFriends = true;
            }
        });

        if (alreadyFriends) {
            res.status(409).send("Users are already befriended.")
        } else {
            User.findOneAndUpdate({ name: userName },
                { $push: { friends: { name: friendName, streak: 0 } } },
                { fields: { name: 1, friends: 1 }, new: true })
                .then(user => res.status(200).json(user));
        }
    })
});

/* POST /user/removefriend */
/* removes the friend of a user if they were friends */
userIO.post('/removefriend', async (req, res) => {
    let { userName, friendName } = req.body;
    User.findOne({ name: userName }, { friends: 1 }).then(user => {
        let wereFriends = false;
        user.friends.some(element => {
            if (element.name === friendName) {
                User.findOneAndUpdate({ name: userName },
                    { $pull: { friends: element } },
                    { fields: { name: 1, friends: 1 }, new: true })
                    .then(user => res.status(200).json(user));
                return wereFriends = true;
            }
        });

        if (!wereFriends) {
            res.status(409).send("Users were not befriended.")
        }
    })
});

/* GET /user/getfriends */
/* get friends by userName */
userIO.get('/getfriends', (req, res) => {
    User.findOne({ name: req.body.userName }, { friends: 1 }).then(friends => res.status(200).json(friends));
});

userIO.post('/updateSettings', async (req, res) => {
    let { name, email, password } = req.body;

    //encrypt the password
    const salt = await bcrypt.genSalt(10);
    if (!salt) throw Error('Something went wrong with bcrypt');
    const hash = await bcrypt.hash(password, salt);
    if (!hash) throw Error('Something went wrong hashing the password');

    const newUserSettings = {
        $set: {
            emailAddress: email,
            password: hash
        }
    }

    //save the user to the db
    User.findOneAndUpdate({ "name": name }, newUserSettings).then(updatedDocument => {
        if (updatedDocument) {
            res.status(200).json(updatedDocument);
        } else {
            res.status(409).send("No matched user found!")
        }
    }).catch(err => res.status(409).send(`Failed to find and update user! ${err}`))
})

/* GET /user/getuser */
/* get user by name */
userIO.get('/getuser', (req, res) => {
    User.findOne({ name: req.body.userName }, { password: 0, __v: 0 }).then(user => res.status(200).json(user));
});

/* GET /user/getpointsofuser */
/* get points for a user by name */
userIO.get('/getpointsofuser', (req, res) => {
    User.findOne({ name: req.body.userName }, { name: 0, points }).then(user => res.status(200).json(user));
});

/* GET /user/getcreatedchallenges */
/* get challenges where the user is the creator of */
userIO.get('/getcreatedchallenges', (req, res) => {
    User.findOne({ name: req.body.userName }, { createdChallenges: 1 })
        .then(challenges =>
            Challenge.find({ _id: { $in: challenges.createdChallenges } }).then(challenges => res.status(200).json(challenges)))
});

/* GET /user/getopenchallenges */
/* get challenges for the user which are still open */
userIO.get('/getopenchallenges', (req, res) => {
    User.findOne({ name: req.body.userName }, { openChallenges: 1 })
        .then(challenges =>
            Challenge.find({ _id: { $in: challenges.openChallenges } }).then(challenges => res.status(200).json(challenges)))
});

/* GET /user/getfinishedchallenges */
/* get challenges the user finished */
userIO.get('/getfinishedchallenges', (req, res) => {
    User.findOne({ name: req.body.userName }, { finishedChallenges: 1 })
        .then(challenges =>
            Challenge.find({ _id: { $in: challenges.finishedChallenges } }).then(challenges => res.status(200).json(challenges)))
});

module.exports = userIO;