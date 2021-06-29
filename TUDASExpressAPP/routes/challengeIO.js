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
    let url = "http://localhost:3030/images/challenges/" + req.file.originalname;
    Challenge.findOne({ name: req.body.challengeName }).then(challenge => {
        if (challenge.dueDate == null || challenge.dueDate <= Date.now()) {
            Challenge.findOneAndUpdate(challenge,
                { $push: { proofmedia: { user: req.body.userName, location: url } } },
                { new: true })
                .then(challenge => res.status(200).json(challenge))
        }
    })
});

/* POST /challenge/uploadsocialmedia */
/* adds a proof picture to a challenge and returns the updated challenge with the picture link */
challengeIO.post('/uploadsocialmedia', async (req, res) => {
    Challenge.findOne({ name: req.body.challengeName }).then(challenge => {
        if (challenge.dueDate == null || challenge.dueDate <= Date.now()) {
            Challenge.findOneAndUpdate({ name: req.body.challengeName },
                { $push: { proofMedia: { user: req.body.userName, location: req.body.url } } },
                { new: true })
                .then(challenge => res.status(200).json(challenge))
        }
    }).catch(err => res.send(err));
});

/* POST /challenge/addchallenge */
/* adds a friend to a user if they are not already friends */
challengeIO.post('/addchallenge', async (req, res) => {
    let { challengeName, creatorName, description, dueDate, reward, isPublic } = req.body;

    let creator = await User.findOne({ name: creatorName })

    const newChallenge = {
        name: challengeName,
        description: description,
        creator: creator._id,
        creationDate: Date.now(),
        dueDate: dueDate,
        reward: reward,
        isPublic: isPublic
    }

    Challenge.create(newChallenge, (err, item) => {
        if (err) {
            res.status(500).send("Challenge could not be created.");
        } else {
            item.save();
            res.status(200).send(item)
        }
    })
    console.log(creator)
});

/* POST /challenge/addchallenge */
/* adds a friend to a user if they are not already friends */
challengeIO.post('/editchallenge', async (req, res) => {
    Challenge.findOne({ name: req.body.challengName }).then(challenge => {
        Challenge
    })
});

/* GET /challenge/getchallenge */
/* get challenge by name */
challengeIO.get('/getchallenge', (req, res) => {
    Challenge.findOne({ name: req.body.challengeName }).then(challenge => res.status(200).json(challenge));
});

module.exports = challengeIO;