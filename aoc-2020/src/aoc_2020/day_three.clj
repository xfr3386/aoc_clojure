(ns aoc-2020.day-three
  (:require [clojure.string :as str]))

(def tree-grid-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/tree-grid.txt")
(def tree-grid-sample-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/tree-grid-sample.txt")

(defn is-tree-at-position [tree-grid-line position move-right-count move-down-count]
  (let [current-position (mod (* position move-right-count) (count tree-grid-line))]
    (if (= (mod position move-down-count) 0)
    (if (= (subs tree-grid-line current-position (+ current-position 1)) "#")
      1
      0)
    0)))

(defn aoc-day3-p1 []
  (let [file-contents    (slurp tree-grid-file)
        tree-grid-lines  (str/split-lines file-contents)]
    (println (reduce + (map-indexed #(is-tree-at-position %2 %1 3 1) tree-grid-lines)))
    )
  )

(defn aoc-day3-p2 []
  (let [file-contents    (slurp tree-grid-file)
        tree-grid-lines  (str/split-lines file-contents)]
    (println (*
    ;; Right 1, down 1.
    (reduce + (map-indexed #(is-tree-at-position %2 %1 1 1) tree-grid-lines))
    ;; Right 3, down 1. (This is the slope you already checked.)
    (reduce + (map-indexed #(is-tree-at-position %2 %1 3 1) tree-grid-lines))
    ;; Right 5, down 1.
    (reduce + (map-indexed #(is-tree-at-position %2 %1 5 1) tree-grid-lines))
    ;; Right 7, down 1.
    (reduce + (map-indexed #(is-tree-at-position %2 %1 7 1) tree-grid-lines))
    ;; Right 1, down 2.
    (reduce + (map-indexed #(is-tree-at-position %2 %1 0.5 2) tree-grid-lines))))))