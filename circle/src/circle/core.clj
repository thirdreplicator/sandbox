(ns circle.core
  (:use [penumbra opengl]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn init [state]
  (app/title! "Circle"))

(defn draw-circle [r p]
  (push-matrix
   (apply translate p)
   (draw-triangle-fan
    (vertex 0 0)
    (doseq [i (range 361)]
      (let [rad (* 2 Math/PI (/ i 360.0))
            x (* r (. Math cos rad))
            y (* r (. Math sin rad))]
        (vertex x y))))))

(defn display [[delta _] state]
  (color 0 1 0)
  (clear-color 1 1 1 1)
  (clear)
  (draw-circle 0.5 [0.0 0.0])
  (app/repaint!))

(defn start []
  (app/start
    {:init init
     :display display } {}))

