(ns nonaga.rules.rings-test
  (:require-macros [cemerick.cljs.test :refer (deftest is are)])
  (:use [nonaga.core :only [initial-game]]
        [nonaga.rules.rings :only [valid-slide? valid-slides can-move-to?
                                   valid-destinations can-be-moved? has-double-gap?]]
        [nonaga.rules.coord :only [nw ne e se sw w]]
        [clojure.set :only [difference intersection]])
  (:require [cemerick.cljs.test :as t]))

(def board-with-gap
  #{[1 1] [3 0] [2 0]})

(deftest which-slides-are-valid
  (are [coord direction valid?]
       (= valid? (valid-slide? (:rings initial-game) coord direction))
       [3 0] ne false
       [3 0] se true)
  (is (not (valid-slide? board-with-gap [2 0] ne)))
  (is (valid-slide? board-with-gap [3 0] ne)))

(deftest enumerate-valid-directions-to-slide
  (is (= 3 (count (valid-slides board-with-gap [2 1])))) )

(deftest path-finding
  (are [valid? board] (= valid? (can-move-to? board [0 2] [4 2]))

       ; No obstructions
       true
       #{[0 2]}

       ; Distination obstructed
       false
       #{[0 2]                   [4 2]}

       ; Non blocking obsticle
       true
       #{[0 2]      [2 2]}

       ; Backtracking obsticle
       true
       #{ [0 3] [1 3] [2 3] [3 3]
        [0 2]             [3 2]
          [0 1] [1 1] [2 1] [3 1]}

       ; Source surrounded
       false
       #{   [-1 4] [0 4] [1 4]
          [-2 3]             [1 3]
        [-2 2]       [0 2]       [2 2]
          [-2 1]             [1 1]
            [-1 0] [0 0] [1 0]}

       ; Source surrounded with gap
       false
       #{   [-1 4] [0 4]
          [-2 3]             [1 3]
        [-2 2]       [0 2]       [2 2]
          [-2 1]             [1 1]
            [-1 0] [0 0] [1 0]}


       ;Destination surrounded
       false
       #{   [3 4] [4 4] [5 4]  
          [2 3]             [5 3]
        [2 2]                   [6 2] 
          [2 1]             [5 1] 
            [3 0] [4 0] [5 0]}

       ;Destination surrounded with gap
       false
       #{   [3 4] [4 4]        
          [2 3]             [5 3]
        [2 2]                   [6 2] 
          [2 1]             [5 1] 
            [3 0] [4 0] [5 0]}))

(deftest knows-where-you-can-go
  (is (= #{[4 3] [1 -1] [4 4] [0 0] [-1 1] [2 5] [-1 3] [4 0] [4 1]
           [2 -1]}
         (valid-destinations (:rings initial-game) [1 4]))))

(deftest knows-what-can-moved
  (is (can-be-moved? initial-game [2 4]))
  (is (not (can-be-moved? initial-game [2 2])))) 


(def one-each-way
  #{   [1 4]       [3 4]  
           [1 3] [2 3] [3 3]
   [0 2] [1 2] [2 2] [3 2] [4 2] 
     [0 1] [1 1] [2 1] [3 1] 
       [1 0] [2 0] [3 0]})

(deftest has-double-gap?-test
  (is (has-double-gap?      one-each-way [1 4]))
  (is (not (has-double-gap? one-each-way [2 2])))
  (is (not (has-double-gap? one-each-way [1 3]))))

