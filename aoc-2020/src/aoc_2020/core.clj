(ns aoc-2020.core
  (:require [aoc-2020.day-one :as day1])
  (:gen-class))

(defn -main []
  (time (day1/aoc-day1-p1))
  (time (day1/aoc-day1-p2)))
