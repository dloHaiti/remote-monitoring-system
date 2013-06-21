package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Delivery
import com.dlohaiti.dloserver.MissingDeliveryAgentException
import grails.converters.JSON

class DeliveriesController {

  def deliveriesService

  def save() {
    log.debug "Received $params"

    Delivery delivery

    try {
      params.kiosk = request.kiosk
      delivery = deliveriesService.saveDelivery(params)
      if (delivery.hasErrors()) {
        render(
            status: 422,
            contentType: 'application/json',
            text: [errors: delivery.errors.fieldErrors.collect({ e -> "${e.field.toUpperCase()}_${e.code.toUpperCase()}" })] as JSON
        )
      } else {
        render(
            status: 201,
            contentType: 'application/json',
            text: [errors: []] as JSON
        )
      }
    } catch (MissingDeliveryAgentException e) {
      log.error("Could not find Agent with name [${params.agentName}] for Kiosk [${params.kiosk.name}]")
      render(
          status: 422,
          contentType: 'application/json',
          text: [errors: ['DELIVERY_AGENT_MISSING']] as JSON
      )
    } catch (Exception e) {
      def date = params.date('createdDate', grailsApplication.config.dloserver.measurement.timeformat.toString())
      log.error("Could not save Delivery on [${date}] from Kiosk [${params.kiosk.name}] with Agent [${params.agentName}]")
      render(
          status: 500,
          contentType: 'application/json',
          text: [errors: ['SERVER_ERROR']] as JSON
      )
    }
  }
}
