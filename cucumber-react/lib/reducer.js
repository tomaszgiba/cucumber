import Gherkin from "gherkin"
import {Map, OrderedMap, List, fromJS} from "immutable"

const parser = new Gherkin.Parser()

const lookupNode = () => "[some gherkin ast node]"

const reducer = (state, action) => {
  if (!state) return new Map({sources: new Map()})

  switch (action.type) {
    case 'start': {
      return state.set('sources', OrderedMap())
    }
    case 'source': {
      const gherkinDocument = parser.parse(action.data)
      return state.setIn(['sources', action.uri], fromJS(gherkinDocument))
    }
    case 'attachment': {
      return state.updateIn(['sources', action.source.uri, 'attachments', action.source.start.line], list => {
        return (list ? list : new List()).push(fromJS({
          uri: action.uri,
          data: action.data,
          media: action.media
        }))
      })
    }
    case 'test-cases': {
      const testCases = action.testCases.map((testCase) => ({
        uri: testCase.uri,
        line: testCase.line,
        status: 'idle',
        result: 'unknown',
        testSteps: testCase.testSteps.map((testStep) => ({
          uri: testStep.uri,
          line: testStep.line,
          status: 'idle',
          result: 'unknown',
        }))
      }))
      state = state.set('testCases', fromJS(testCases))
      return state
    }
    default: {
      throw new Error("Unsupported action: " + JSON.stringify(action))
    }
  }
}

export default reducer
