package allison.zipcode


class StateController {


    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [stateInstanceList: State.list(params), stateInstanceTotal: State.count()]
    }

    /**
     * Show the states.
     * @param id
     * @return
     */
    def show(Long id) {
        def stateInstance
        if (params.stateName) { // This was a link from the tag cloud so translate it
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
