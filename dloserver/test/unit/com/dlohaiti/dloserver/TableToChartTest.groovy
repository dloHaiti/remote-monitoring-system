package com.dlohaiti.dloserver

import org.junit.Test

class TableToChartTest {

  def sampleTable = [
      ["Dates","06/04/13","06/05/13","06/06/13","06/07/13","06/08/13","06/09/13","06/10/13"],
      ["2GALLON",0,0,0,0,0,0,7.5],
      ["5GALLON",0,0,0,0,0,0,0],
      ["10GALLON",0,0,0,0,0,0,13.5],
      ["DELIVERY",0,0,0,0,0,0,2],
      ["TOTAL",0,0,0,0,0,0,23]
  ]

  @Test
  void shouldConvertToFormAcceptedByGoogleCharts() {
    def tableToChart = new TableToChart()
    def chartData = tableToChart.convertWithoutRowsTitled(sampleTable, ['TOTAL'])
    assert [
        ["Dates","2GALLON","5GALLON","10GALLON","DELIVERY"],
        ["06/04/13",0,0,0,0],
        ["06/05/13",0,0,0,0],
        ["06/06/13",0,0,0,0],
        ["06/07/13",0,0,0,0],
        ["06/08/13",0,0,0,0],
        ["06/09/13",0,0,0,0],
        ["06/10/13",7.5,0,13.5,2]
    ] == chartData
  }

  @Test
  void shouldConvertWithoutSpecifiedRows() {
    def tableToChart = new TableToChart()
    def chartData = tableToChart.convertWithoutRowsTitled(sampleTable, ['TOTAL', 'DELIVERY', '10GALLON'])
    assert [
        ["Dates","2GALLON","5GALLON"],
        ["06/04/13",0,0],
        ["06/05/13",0,0],
        ["06/06/13",0,0],
        ["06/07/13",0,0],
        ["06/08/13",0,0],
        ["06/09/13",0,0],
        ["06/10/13",7.5,0]
    ] == chartData
  }
}
