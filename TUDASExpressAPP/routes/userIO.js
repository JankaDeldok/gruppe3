var express = require('express');
var userIO = express.Router();

var multer = require("multer");
var fs = require('fs');


const User = require('../models/userSchema');


// Storage for saving pictures on the server.
var storageTemplate = multer.diskStorage({
    destination: './public/images/templates',
    filename: function (req, file, cb) {
        cb(null, file.originalname);
    }
});

var uploadProfilePic = multer({ storage: storageTemplate });

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

// Work in Progress, only serves as a template
/* POST /userIO/upload-picture */
userIO.post('/upload-picture', uploadProfilePic.single("file"), auth, async (req, res) => {
    //get the user from the db
    let user = await User.findById(req.user.id);
});


module.exports = userIO;