(ns meiro.backtracker
  "The Recursive Backtracker algorithm uses a biased random walk to generate a
  maze. While there are unvisited cells, it chooses one at random, but when it
  encounters a dead end, it backtracks on its path until it finds a cell with
  unvisited neighbors and walks, repeating until all cells have been linked."
  (:require [meiro.core :as m]))

(defn create
  "Create a random maze using the Recursive Backtracker algorithm.
  If a `pos` is passed, then the random walk will begin at that position."
  ([grid] (create grid (m/random-pos grid)))
  ([grid pos] (create grid pos m/empty-neighbors m/link))
  ([grid pos empty-fn link-fn]
   (loop [maze grid
          pos pos
          stack '(pos)]
     (if (seq stack)
       (let [unvisited (empty-fn maze pos)]
         (if (seq unvisited)
           (let [neighbor (rand-nth unvisited)]
             (recur (link-fn maze pos neighbor) neighbor (conj stack neighbor)))
           (recur maze (first stack) (rest stack))))
       maze))))
