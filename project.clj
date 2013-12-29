(defproject nonaga "0.1.0-SNAPSHOT"
  :description "A Clojure implementation of the game Spanish table top game
               Nonaga."
  :url "https://github.com/logaan/nonaga"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "6969"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-cljsbuild "1.0.1"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:optimizations :whitespace
                                   :pretty-print true}}]})
