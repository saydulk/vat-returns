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

package appRoutes

import base.SpecBase
import models.VatReturnFilters

class AppRoutesSpec extends SpecBase {

  "The reverse route for VatReturnsController.getVatReturns" should {

    "for the specified vrn" when {
      lazy val vrn = "555555555"

      "'period-key' query parameter is supplied" should {

        lazy val queryParams: VatReturnFilters = VatReturnFilters(periodKey = "17AA")

        val expected = "/vat-returns/returns/vrn/555555555/?period-key=17AA"

        s"have the route '$expected'" in {
          val route: String = controllers.routes.VatReturnsController.getVatReturns(vrn, queryParams).url
          route shouldBe expected
        }
      }
    }
  }
}