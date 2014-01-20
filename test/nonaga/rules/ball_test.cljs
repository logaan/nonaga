(ns nonaga.rules.ball-test
  (:require-macros [cemerick.cljs.test :refer (deftest is are)])
  (:use [nonaga.core :only [initial-game]]
        [nonaga.rules.ball :only [invalid-space? move valid-destinations]]
        [nonaga.rules.coord :only [nw ne e se sw w]] )
  (:require [cemerick.cljs.test :as t]))

(deftest invalid-space
  (are [coord expected]
       (= expected (invalid-space? initial-game coord))

       [0 4] true
       [1 4] true
       [2 4] false))

(deftest movement
  (are [coord direction expected]
       (= expected (move initial-game coord direction))

       [3 0] nw [1 3]
       [0 3] e  [3 3]
       [0 1] e  [3 1])

  (is (= #{[2 1] [2 4] [0 3]} (valid-destinations initial-game [1 4]))))

