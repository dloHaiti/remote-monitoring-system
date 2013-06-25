package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.MissingSamplingSiteException
import com.dlohaiti.dloserver.Reading
import grails.converters.JSON

class ReadingsController {

  def readingsService

  def save() {
    log.debug "Received $params"

    Reading reading

    try {
      params.kiosk = request.kiosk
      reading = readingsService.saveReading(params)

      if (reading.hasErrors()) {
        log.debug(reading.errors)
        render(status: 422,
            contentType: 'application/json',
            text: [errors: reading.errors.fieldErrors.collect({e -> "${e.field.toUpperCase()}_${e.code.toUpperCase()}"})] as JSON
        )
      } else {
        render(
            status: 201,
            contentType: 'application/json',
            text: [errors: []] as JSON
        )
      }
    } catch(MissingSamplingSiteException ignore) {
      log.error("Error saving Reading. SamplingSite [${params.samplingSiteName}] not found.")
      render(
          status: 422,
          contentType: 'application/json',
          text: [errors:['MISSING_SAMPLING_SITE']] as JSON
      )
    } catch (Exception e) {
      def createdDate = params.date('createdDate', grailsApplication.config.dloserver.measurement.timeformat.toString())
      log.error("Error saving Reading [${createdDate}]: ", e)
      render(
          status: 500,
          contentType: 'application/json',
          text: [errors: ['SERVER_ERROR']] as JSON
      )
    }
  }
}
