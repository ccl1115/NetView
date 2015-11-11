var express = require('express');
var router = express.Router();
var fs = require('fs');

/* GET android view listing. */
router.get('/', function(req, res) {
  res.send('respond with a resource');
});

router.get('/:id/:variant', function(req, res) {
  
  var avId = res.params.id;
  var variant = res.params.variant;

  if (avId && variant) {
    var path = 'av/' + avId + '/' + variant;
    fs.stat(path, function(err, stat) {
      if (!err && stat.isFile()) {
        res.sendFile(path);
      } else {
        res.status(404).send('404, page not found');
      }
    });
  }
  res.status(404).send('404, page not found');
});

module.exports = router;
