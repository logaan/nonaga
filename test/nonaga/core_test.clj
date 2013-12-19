(ns nonaga.core-test
  (:require [clojure.test :refer :all]
            [nonaga.core :refer :all]))

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
       [0 1] e  [3 1]))

(run-tests)
