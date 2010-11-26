(ns circle.core
  (:use [penumbra opengl]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn reset-state [state]
  (assoc state :p [-0.5 -0.5]
               :v [0.0 0.0]))

(defn init [state]
  (app/vsync! true)
  (app/title! "Circle")
  (reset-state state))

(defn draw-circle [p]
  (push-matrix
   (apply translate p)
   (draw-triangle-strip
    (vertex 0 0)
    (vertex 0.5 1)
    (vertex 1 0))))

(defn reshape [_ state]
  (ortho-view -1 1 -1 1 -1 1)
  state)

(defn display [[delta _] state]
  (color 0 0 0)
  (clear-color 1 1 1 1)
  (clear)
  (draw-circle (:p state))
  (app/repaint!))

(defn move [state direction]
  (let [p (:p state)
        x (p 0)
        y (p 1)]
  (cond 
    (= :left direction)
      (assoc state :p [(- x 0.1) y])
    (= :right direction)
      (assoc state :p [(+ x 0.1) y])
    (= :down direction)
      (assoc state :p [x (- y 0.1)])
    (= :up direction)
      (assoc state :p (assoc p 1 (+ y 0.1))))))

(defn key-press [key state]
 (cond
  (= key :up) 
    (move state :up)
  (= key :down) 
    (move state :down)
  (= key :left) 
    (move state :left)
  (= key :right) 
    (move state :right)
  :else 
    state))


(defn start []
  (app/start 
    {:init init 
     :display display
     :reshape reshape
     :key-press key-press } {})) 
