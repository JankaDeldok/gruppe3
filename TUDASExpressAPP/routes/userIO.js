var express = require('express');
var userIO = express.Router();

var multer = require("multer");
var fs = require('fs');


const User = require('../models/userSchema');


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


/* POST /user/upload-picture */
/* adds a profile picture to a user and returns the updated user with the picture link */
userIO.post('/upload-picture', profileImg.single("file"), async (req, res) => {
    //get the user from the db
    let url = "http://localhost:3030/images/profiles/" + req.file.originalname;
    User.findOneAndUpdate({ name: req.body.userName },
        { profilepicture: url },
        { fields: { password: 0 }, new: true })
        .then(user => res.status(200).json(user))
});

/* POST /user/addfriend */
/* adds a friend to a user if they are not already friends */
userIO.post('/addfriend', async (req, res) => {
    User.findOne({ name: req.body.userName }, { friends: 1 }).then(user => {
        let alreadyFriends = false;
        user.friends.some(element => {
            if (element.name === req.body.friendName) {
                return alreadyFriends = true;
            }
        });

        if (alreadyFriends) {
            res.status(409).send("Users are already befriended.")
        } else {
            User.findOneAndUpdate({ name: req.body.userName },
                { $push: { friends: { name: req.body.friendName, streak: 0 } } },
                { fields: { name: 1, friends: 1 }, new: true })
                .then(user => res.status(200).json(user));
        }
    })
});

/* POST /user/removefriend */
/* removes the friend of a user if they were friends */
userIO.post('/removefriend', async (req, res) => {
    User.findOne({ name: req.body.userName }, { friends: 1 }).then(user => {
        let wereFriends = false;
        user.friends.some(element => {
            if (element.name === req.body.friendName) {
                User.findOneAndUpdate({ name: req.body.userName },
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

/* GET /user/getuser */
/* get user by name */
userIO.get('/getuser', (req, res) => {
    User.findOne({ name: req.body.userName }, { password: 0, __v: 0 }).then(user => res.status(200).json(user));
});

/* GET /user/getfriends */
/* get user by name */
userIO.get('/getfriends', (req, res) => {
    User.findOne({ name: req.body.userName }, { friends: 1 }).then(friends => res.status(200).json(friends));
});

module.exports = userIO;