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
challengeIO.post('/upload-picture', storageProofImg.single("file"), async (req, res) => {
    //get the user from the db
    let user = await User.findById(req.user.id);
});

/* POST /user/addchallenge */
/* adds a friend to a user if they are not already friends */
challengeIO.post('/addchallenge', async (req, res) => {
    User.findOne({ name: req.body.userName }, { friends: 1 }).then(user => {
        let alreadyFriends = false;
        user.friends.some(element => {
            if (element.name === req.body.friendName) {
                return alreadyFriends = true;
            }
        });

        if (alreadyFriends) {
            res.status(409).send("User is already befriended.")
        } else {
            User.updateOne({ name: req.body.userName }, { $push: { friends: { name: req.body.friendName, streak: 0 } } }).then(user => res.status(200).json(user));
        }
    })
});

/* GET /user/getuser */
/* get user by name */
challengeIO.get('/getuser', (req, res) => {
    User.findOne({ name: req.body.userName }, { password: 0, __v: 0 }).then(user => res.status(200).json(user));
});

module.exports = userIO;