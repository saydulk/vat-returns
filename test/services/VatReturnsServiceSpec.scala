/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import base.SpecBase
import mocks.connectors.MockVatReturnsConnector
import models._
import play.api.http.Status

class VatReturnsServiceSpec extends SpecBase with MockVatReturnsConnector {

  object TestVatReturnsService extends VatReturnsService(mockVatReturnsConnector)

  lazy val exampleVrn = "555555555"

  "The VatReturnService.getVatReturns method" should {

    "Return a VatReturn when a success response is returned from the Connector" in {

      val vatReturn: VatReturn = VatReturn(
        VatReturnIdentification("VRN", exampleVrn),
        VatReturnDetail(
          "17AA",
          1.23,
          1.23,
          1.23,
          1.23,
          1.23,
          1.23,
          1.23,
          1.23,
          1.23
        )
      )

      val successResponse: Either[Nothing, VatReturn] = Right(vatReturn)
      val queryParams: VatReturnFilters = VatReturnFilters(
        periodKey = "17AA"
      )

      setupMockGetVatReturns(exampleVrn, queryParams)(successResponse)

      val actual: Either[ErrorResponse, VatReturn] = await(TestVatReturnsService.getVatReturns(
        exampleVrn,
        VatReturnFilters(
          periodKey = "17AA"
        )
      ))

      actual shouldBe successResponse
    }

    "Return Error when a single error is returned from the Connector" in {

      val singleErrorResponse: Either[ErrorResponse, Nothing] =
        Left(ErrorResponse(Status.BAD_REQUEST, Error("CODE", "MESSAGE")))

      setupMockGetVatReturns(exampleVrn, VatReturnFilters(
        periodKey = "17AA"
      ))(singleErrorResponse)

      val actual: Either[ErrorResponse, VatReturn] = await(TestVatReturnsService.getVatReturns(
        exampleVrn,
        VatReturnFilters(
          periodKey = "17AA"
        )
      ))

      actual shouldBe singleErrorResponse
    }

    "Return a MultiError when multiple error responses are returned from the Connector" in {

      val multiErrorResponse: Either[ErrorResponse, Nothing] = Left(ErrorResponse(Status.BAD_REQUEST, MultiError(Seq(
        Error("CODE 1", "MESSAGE 1"),
        Error("CODE 2", "MESSAGE 2")
      ))))

      setupMockGetVatReturns(exampleVrn, VatReturnFilters(
        periodKey = "17AA"
      ))(multiErrorResponse)

      val actual: Either[ErrorResponse, VatReturn] = await(TestVatReturnsService.getVatReturns(
        exampleVrn,
        VatReturnFilters(
          periodKey = "17AA"
        )
      ))

      actual shouldBe multiErrorResponse
    }
  }
}
