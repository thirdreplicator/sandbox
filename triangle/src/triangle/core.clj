(ns triangle.core
  (:use [penumbra opengl]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn reset-state [state]
  (assoc state :p [-0.5 -0.5]))

(defn init [state]
  (app/vsync! true)
  (app/title! "Triangle")
  (reset-state state))

(defn draw-triangle [p]
  (push-matrix
   (apply translate p)
   (draw-triangle-strip
  (color 1 0 0)
    (vertex 0 0)
  (color 0 1 0)
    (vertex 0.5 1)
  (color 0 0 1)
    (vertex 1 0))))

(defn reshape [_ state]
  (ortho-view -1 1 -1 1 -1 1)
  state)

(defn display [[delta _] state]
  (color 0 0 0)
  (clear-color 1 1 1 1)
  (clear)
  (draw-triangle (:p state))
  (app/repaint!))

(defn start []
  (app/start 
    {:init init 
     :display display
     :reshape reshape } {})) 
