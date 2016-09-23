import Stream from "stream"
import React from "react"
import {render} from "enzyme"
import {Cucumber} from "cucumber-react"

class ReactOutput extends Stream.Writable {
  constructor() {
    super({objectMode: true})
  }

  _write(state, _, callback) {
    this._state = state
    callback()
  }

  getFeatureNames() {
    const cucumber = render(<Cucumber sources={this._state.get('sources')}/>)
    return Promise.resolve(cucumber.find('.feature > .name').text())
  }
}

export default ReactOutput
