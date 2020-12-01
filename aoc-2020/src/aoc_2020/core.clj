(ns aoc-2020.core
   ;(:require [clojure.java.io :as io])
   (:require [clojure.string :as str])
   (:gen-class))
 
(def expense-report "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/expense-report.txt")

(defn find-2020-sum-and-mult [values]
  (let [first-value (first values)
        last-value (nth values (- (count values) 1))
        first-last-sum (+ first-value last-value)
        sub-2020-from-sum (- 2020 first-last-sum)]
    (if (= sub-2020-from-sum 0)
      (* first-value last-value)
      (if (< sub-2020-from-sum 0)
        (find-2020-sum-and-mult (drop-last values))
        (find-2020-sum-and-mult (rest values))))
    )
  )

(defn aoc-day-1 "AOC day 1" [] 
 (let [file-contents    (slurp expense-report)
       nums-as-strings  (str/split-lines file-contents)
       expense-values   (map read-string nums-as-strings)
       sorted-values    (sort < expense-values)]
   (find-2020-sum-and-mult sorted-values)))

(defn -main []
  (aoc-day-1))
