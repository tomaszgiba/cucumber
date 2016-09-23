import Stream from "stream"
import React from "react"
import {render} from "enzyme"
import {CucumberReact} from "cucumber-react"
const {Cucumber} = CucumberReact

class ReactOutput extends Stream.Writable {
  constructor() {
    super({objectMode: true})
  }

  _write(state, _, callback) {
    this._state = state
    callback()
  }

  getFeatureNames() {
    const cucumber = render(<Cucumber state={this._state}/>)
    return Promise.resolve(cucumber.find('.feature > .name').text())
  }
}

export default ReactOutput
