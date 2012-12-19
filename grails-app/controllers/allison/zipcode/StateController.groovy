package allison.zipcode

import org.springframework.dao.DataIntegrityViolationException

class StateController {


    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [stateInstanceList: State.list(params), stateInstanceTotal: State.count()]
    }


    def show(Long id) {
        def stateInstance
        if (params.stateName) {
            stateInstance = ZipcodeService.getState(Country.get(id), params.stateName)
        } else {
            stateInstance = State.get(id)
        }
        if (!stateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'state.label', default: 'State'), id])
            redirect(action: "list")
            return
        }

        [stateInstance: stateInstance]
    }

}
