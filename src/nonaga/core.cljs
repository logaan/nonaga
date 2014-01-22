(ns nonaga.core)

(def initial-game
  {:rings
    #{   [1 4] [2 4] [3 4]  
       [0 3] [1 3] [2 3] [3 3]
     [0 2] [1 2] [2 2] [3 2] [4 2] 
       [0 1] [1 1] [2 1] [3 1] 
         [1 0] [2 0] [3 0]}
  
   :red
    #{   [1 4]
    
                             [4 2] 
     
         [1 0]}
  
   :blue
    #{               [3 4]  
       
     [0 2]
       
                     [3 0]}})                   

(defn move-ball [game color from to]
  (update-in game [color] #(-> % (disj from) (conj to))))

(defn move-ring [game from to]
  (update-in game [:rings] #(-> % (disj from) (conj to))))

