(ns aoc-2020.day-nine
  (:require [clojure.string :as str]))

(def xmas-encryption-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/xmas-encryption.txt")

;; modified from day 1
;; find two values in sorted-values whose sum is desired-sum
(defn find-sum-to-value [sorted-values desired-sum]
  (let [first-value (first sorted-values)
        last-value (nth sorted-values (- (count sorted-values) 1))
        first-last-sum (+ first-value last-value)
        sub-desired-from-sum (- desired-sum first-last-sum)]
    (if (<= (count sorted-values) 1)
      nil
      (if (= sub-desired-from-sum 0)
        (list first-value last-value)
        (if (< sub-desired-from-sum 0)
          (find-sum-to-value (drop-last sorted-values) desired-sum)
          (find-sum-to-value (rest sorted-values) desired-sum))))))

;; challenge: given a list of numbers, find a number that is not the sum
;; of two of the previous 25 numbers
(defn aoc-day9-p1 []
  (let [xmas-file    (slurp xmas-encryption-file)
        xmas-file-contents  (into [] (map read-string (str/split-lines xmas-file)))
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


(defn find-contiguous-sum-values [values desired-sum starting-index]
  (loop [index starting-index
         sum 0]
    (if (= index (- (count values) 1))
      nil
      (let [current-sum (+ sum (nth values index))]
        (if (= current-sum desired-sum)
          (subvec values starting-index index)
          (if (> current-sum desired-sum)
            nil
            (recur (inc index) current-sum)))))))

;; challenge: find contiguous numbers that sum up to the result from part 1 - 29221323
;; sample file part 1 result is 127
(defn aoc-day9-p2 []
  (let [xmas-file    (slurp xmas-encryption-file)
        xmas-file-contents  (into [] (map read-string (str/split-lines xmas-file)))
        sum-to-find 29221323]
    (loop [index 0]
      (if (= index (- (count xmas-file-contents) 1))
        nil
        (let [contiguous-sum (find-contiguous-sum-values xmas-file-contents sum-to-find index)]
          (if (nil? contiguous-sum)
            (recur (inc index))
            (let [sorted-values (sort < contiguous-sum)
                  first-value (first sorted-values)
                  last-value (last sorted-values)]
              (println (+ first-value last-value)))))))))
