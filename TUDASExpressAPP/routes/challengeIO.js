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


// Work in Progress, only serves as a template
/* POST /user/upload-picture */
challengeIO.post('/upload-picture', proofImg.single("file"), async (req, res) => {
    //get the user from the db
    let user = await User.findById(req.user.id);
});

/* POST /user/addchallenge */
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
            console.log(err)
            res.status(500).send("Challenge could not be created.");
        } else {
            item.save();
            res.status(200).send(item)
        }
    })
    console.log(creator)
});

/* GET /challenge/getchallenge */
/* get challenge by name */
challengeIO.get('/getchallenge', (req, res) => {
    Challenge.findOne({ name: req.body.challengeName }).then(challenge => res.status(200).json(challenge));
});

module.exports = challengeIO;