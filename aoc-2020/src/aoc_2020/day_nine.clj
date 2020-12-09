(ns aoc-2020.day-nine
  (:require [clojure.string :as str]))

(def xmas-encryption-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/xmas-encryption.txt")
(def xmas-encryption-sample-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/xmas-encryption-sample.txt")

;; copied from day 1
;; find two values in sorted-values whose sum is desired-sum
(defn find-sum-to-value [sorted-values desired-sum]
  (let [first-value (first sorted-values)
        last-value (nth sorted-values (- (count sorted-values) 1))
        first-last-sum (+ first-value last-value)
        sub-desired-from-sum (- desired-sum first-last-sum)]
    (if (<= (count sorted-values) 1) ;if 1 or fewer items return empty list
      nil
      (if (= sub-desired-from-sum 0)
        (list first-value last-value)
        (if (< sub-desired-from-sum 0)
          (find-sum-to-value (drop-last sorted-values) desired-sum)
          (find-sum-to-value (rest sorted-values) desired-sum))))))

(defn aoc-day9-p1 []
  (let [xmas-file    (slurp xmas-encryption-file)
        xmas-file-contents  (into [] (map read-string (str/split-lines xmas-file)))
        xmas-sample-file    (slurp xmas-encryption-sample-file)
        xmas-sample-file-contents  (into [] (map read-string (str/split-lines xmas-sample-file)))
        preamble-length 25]

    (loop [index preamble-length]
      (if (= index (- (count xmas-file-contents) 1))
        (println "none found")
        (let [previous-numbers (sort < (subvec xmas-file-contents (- index preamble-length) index))
              current-number (nth xmas-file-contents index)
              sum-results (find-sum-to-value previous-numbers current-number)]
          (if (some? sum-results)
            (recur (inc index))
            (println current-number)))))))
