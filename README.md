# nonaga

A Clojure implementation of the game Spanish table top game [Nonaga] [bgg].

A playable copy is available at http://logaan.net/nonaga.

## Local Deployment

1. `lein cljsbuild auto`
2. Open `resources/index.html`

## Heroku Deployment

1. Follow the [standard Heroku Clojure instructions](https://devcenter.heroku.com/articles/getting-started-with-clojure)
2. Go into the "Settings" for the app and set the following buildpacks in order:
  1. https://github.com/heroku/heroku-buildpack-clojure
  2. https://github.com/heroku/heroku-buildpack-static
3. Create a Config Variable `LEIN_BUILD_TASK` as `cljsbuild once`

## License

Copyright © 2013 Logan Campbell

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[bgg]: http://boardgamegeek.com/boardgame/46614/nonaga


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/logaan/nonaga/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

