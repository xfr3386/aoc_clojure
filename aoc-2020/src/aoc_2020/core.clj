(ns aoc-2020.core
  (:require [aoc-2020.day-one :as day1])
  (:require [aoc-2020.day-two :as day2])
  ;; (:require [aoc-2020.day-three :as day3])
  (:require [aoc-2020.day-four :as day4])
  (:require [aoc-2020.day-five :as day5])
  (:gen-class))

(defn -main []
  (println "==============" "AOC2020 Day 1" "==============")
  (time (day1/aoc-day1-p1))
  (time (day1/aoc-day1-p2))

  (println "==============" "AOC2020 Day 2" "==============")
  (time (day2/aoc-day2-p1-reduce))
  (time (day2/aoc-day2-p2-reduce))

  (println "==============" "AOC2020 Day 3" "==============")

  (println "==============" "AOC2020 Day 4" "==============")
  (time (day4/aoc-day4-p1))
  (time (day4/aoc-day4-p2))

  (println "==============" "AOC2020 Day 5" "==============")
  (time (day5/aoc-day5-p1))
  (time (day5/aoc-day5-p2)))


