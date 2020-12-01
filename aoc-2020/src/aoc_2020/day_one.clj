(ns aoc-2020.day-one
  (:require [clojure.string :as str]))

(def expense-report "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/expense-report.txt")

(defn find-sum-to-value [sorted-values desired-sum]
  (let [first-value (first sorted-values)
        last-value (nth sorted-values (- (count sorted-values) 1))
        first-last-sum (+ first-value last-value)
        sub-desired-from-sum (- desired-sum first-last-sum)]
    (if (<= (count sorted-values) 1) ;if 1 or fewer items return empty list
      (list)
      (if (= sub-desired-from-sum 0)
        (list first-value last-value)
        (if (< sub-desired-from-sum 0)
          (find-sum-to-value (drop-last sorted-values) desired-sum)
          (find-sum-to-value (rest sorted-values) desired-sum))))))

(defn remove-item-by-index [values index]
  (concat (subvec values 0 index)
          (subvec values (inc index))))

(defn p2-main-function
  ([sorted-values]
   (p2-main-function sorted-values 0))
  ([sorted-values index]
   (let [fixed-value (nth sorted-values index)
         remaining-sum (- 2020 fixed-value)
         list-without-fixed (remove-item-by-index (into [] sorted-values) index)
         other-values (find-sum-to-value list-without-fixed remaining-sum)]
     (if (not-empty other-values)
       (reduce * fixed-value other-values)
       (p2-main-function sorted-values (inc index))))))

(defn aoc-day1-p1 "AOC day 1 part 1" []
  (let [file-contents    (slurp expense-report)
        nums-as-strings  (str/split-lines file-contents)
        expense-values   (map read-string nums-as-strings)
        sorted-values    (sort < expense-values)]
    (println (reduce * (find-sum-to-value sorted-values 2020)))))

(defn aoc-day1-p2 "AOC day 1 part 2" []
  (let [file-contents    (slurp expense-report)
        nums-as-strings  (str/split-lines file-contents)
        expense-values   (map read-string nums-as-strings)
        sorted-values    (sort < expense-values)]
    (println (p2-main-function sorted-values))))