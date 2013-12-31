(ns nonaga.rules.coord-test
  (:require-macros [cemerick.cljs.test :refer (deftest is are)])
  (:use [nonaga.rules.coord :only [nw ne e se sw w neighbours]])
  (:require [cemerick.cljs.test :as t]))

(deftest directional-functions
  (are [function start expected]
       (= expected (take 5 (iterate function start)))

       nw [3 0] [[3 0] [2 1] [2 2] [1 3] [1 4]]
       ne [1 0] [[1 0] [1 1] [2 2] [2 3] [3 4]]
       e  [0 2] [[0 2] [1 2] [2 2] [3 2] [4 2]]
       se [1 4] [[1 4] [1 3] [2 2] [2 1] [3 0]]
       sw [3 4] [[3 4] [2 3] [2 2] [1 1] [1 0]]
       w  [4 2] [[4 2] [3 2] [2 2] [1 2] [0 2]]))

(deftest neighbouring-cells
  (is (= #{[1 3] [2 3] [1 2] [3 2] [1 1] [2 1]} (neighbours [2 2]))))


