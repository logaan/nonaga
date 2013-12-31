(defproject nonaga "0.1.0-SNAPSHOT"
  :description "A Clojure implementation of the game Spanish table top game
               Nonaga."
  :url "https://github.com/logaan/nonaga"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "6969"]
                 [prismatic/dommy "0.1.1"]
                 [com.cemerick/clojurescript.test "0.2.1"]]
  :plugins [[lein-cljsbuild "1.0.1"]
            [com.cemerick/clojurescript.test "0.2.1"]]
  :hooks  [leiningen.cljsbuild]
  :cljsbuild {:builds {:dev {:source-paths ["src"]
                             :compiler {:output-to "resources/js/main.js"
                                        :optimizations :whitespace
                                        :pretty-print true}}
                       :test {:source-paths ["src" "test"]
                              :compiler {:output-to "target/cljs/unit-test.js"
                                         :optimizations :whitespace
                                         :pretty-print true}}}
              :test-commands {"unit" ["phantomjs" :runner 
                                      "window.literal_js_was_evaluated=true"
                                      "target/cljs/unit-test.js"]}})

