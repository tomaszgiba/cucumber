/* eslint-env browser */
import React from "react"
import {createStore} from "redux"
import {Provider} from "react-redux"
import {render} from "react-dom"
import {CucumberReact, reducer} from "cucumber-react"
const {Cucumber} = CucumberReact

const store = createStore(reducer).getState()

render(
  <Provider><Cucumber state={store.getState()}/></Provider>,
  document.getElementById('app')
)

const es = new EventSource('/sse')
es.onmessage = function (messageEvent) {
  const event = JSON.parse(messageEvent.data)
  store.dispatch(event)
}
