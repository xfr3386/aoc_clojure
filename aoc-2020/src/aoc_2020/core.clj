(ns aoc-2020.core
  (:require [aoc-2020.day-one :as day1])
  (:require [aoc-2020.day-two :as day2])
  (:require [aoc-2020.day-three :as day3])
  (:require [aoc-2020.day-four :as day4])
  (:require [aoc-2020.day-five :as day5])
  (:require [aoc-2020.day-six :as day6])
  (:require [aoc-2020.day-seven :as day7])
  (:require [aoc-2020.day-eight :as day8])
  (:gen-class))

  (defn -main []
    (println "==============" "AOC2020 Day 1" "==============")
    (time (day1/aoc-day1-p1))
    (time (day1/aoc-day1-p2))
    (println)

    (println "==============" "AOC2020 Day 2" "==============")
    (time (day2/aoc-day2-p1-reduce))
    (time (day2/aoc-day2-p2-reduce))
    (println)

    (println "==============" "AOC2020 Day 3" "==============")
    (time (day3/aoc-day3-p1))
    (time (day3/aoc-day3-p2))
    (println)

    (println "==============" "AOC2020 Day 4" "==============")
    (time (day4/aoc-day4-p1))
    (time (day4/aoc-day4-p2))
    (println)

    (println "==============" "AOC2020 Day 5" "==============")
    (time (day5/aoc-day5-p1))
    (time (day5/aoc-day5-p2))
    (println)

    (println "==============" "AOC2020 Day 6" "==============")
    (time (day6/aoc-day6-p1))
    (time (day6/aoc-day6-p2))
    (println)

    (println "==============" "AOC2020 Day 7" "==============")
    (time (day7/aoc-day7-p1))
    (println "45157")

    (println "==============" "AOC2020 Day 8" "==============")
    (time (day8/aoc-day8-p1))
    (time (day8/aoc-day8-p2)))


