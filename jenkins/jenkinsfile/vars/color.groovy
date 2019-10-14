#!/usr/bin/env groovy
// ===========================================================================
//     FileName: color.groovy
//       Author: marslo.jiao@gmail.com
//      Created: 2019-07-04 08:15:24
//   LastChange: 2019-10-14 13:07:53
// ===========================================================================

/**
 * print colorful string in Console Output
 * Usage: println color.show('red', 'my string')
 *
 * @param colorName : Support color: red | green | yellow | blue | magenta | cyan | white | brblack | brred | brgreen | bryellow | brblue | brmagenta | brcyan | brwhite | brwhite.
 * @param str : the str you want to print
 */
def show(String colorName, String str) {
  colorMatrix = [
            'black':'\u001B[30m' ,
              'red':'\u001B[31m' ,
            'green':'\u001B[32m' ,
           'yellow':'\u001B[33m' ,
             'blue':'\u001B[34m' ,
          'magenta':'\u001B[35m' ,
             'cyan':'\u001B[36m' ,
            'white':'\u001B[37m' ,
          'brblack':'\u001B[90m' ,
            'brred':'\u001B[91m' ,
          'brgreen':'\u001B[92m' ,
         'bryellow':'\u001B[93m' ,
           'brblue':'\u001B[94m' ,
        'brmagenta':'\u001B[95m' ,
           'brcyan':'\u001B[96m' ,
          'brwhite':'\u001B[97m' ,
              'ESC':'\u001B[0m'
  ]

  if (colorMatrix.containsKey(colorName)) {
    return colorMatrix[colorName] + str + colorMatrix['ESC']
  } else {
    return colorMatrix['red'] + "WARN: the color name: ${colorName} cannot be found in matrix" + colorMatrix['ESC']
  }
}

// vim: ft=Jenkinsfile ts=2 sts=2 sw=2 et
