/*
 * Copyright 2012 Roman Nurik
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

package com.example.android.wizardpager;

import android.content.Context;

import com.example.android.wizardpager.pages.CustomerInfoPage;
import com.tech.freak.wizardpager.model.AddRemoveItemsPage;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.ProductItem;
import com.tech.freak.wizardpager.model.Restaurant;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SandwichWizardModel extends AbstractWizardModel {
    Restaurant lTim;

    public SandwichWizardModel(Context context) {
        super(context);
        }

        @Override
    protected PageList onNewRootPageList() {
            lTim = new Restaurant("Tim Hortons");
            lTim.setProducts(new LinkedHashMap<String, ArrayList<ProductItem>>()
                             {{
                                     put("Food", new ArrayList<ProductItem>() {{
                                         add(new ProductItem("Bagel",3.0));
                                         add(new ProductItem("Danish",3.5));
                                         add(new ProductItem("Sandwich",4.0));
                                     }});

                                     put("Drink", new ArrayList<ProductItem>() {{
                                         add(new ProductItem("Coffee",1.99));
                                         add(new ProductItem("Tea",2.5));
                                         add(new ProductItem("Water",2.0));
                                     }});
                                 }}

            );

        return new PageList(new BranchPage(this, "Resturant").addRestaurant(lTim,this).addBranch(
                "Sandwich",
                new SingleFixedChoicePage(this, "Bread").setChoices("White",
                        "Wheat", "Rye", "Pretzel", "Ciabatta")
                        .setRequired(true),

                new MultipleFixedChoicePage(this, "Meats").setChoices(
                        "Pepperoni", "Turkey", "Ham", "Pastrami", "Roast Beef",
                        "Bologna"),

                new MultipleFixedChoicePage(this, "Veggies").setChoices(
                        "Tomatoes", "Lettuce", "Onions", "Pickles",
                        "Cucumbers", "Peppers"),

                new MultipleFixedChoicePage(this, "Cheeses").setChoices(
                        "Swiss", "American", "Pepperjack", "Muenster",
                        "Provolone", "White American", "Cheddar", "Bleu"),

                new BranchPage(this, "Toasted?")
                        .addBranch(
                                "Yes",
                                new SingleFixedChoicePage(this, "Toast time")
                                        .setChoices("30 seconds", "1 minute",
                                                "2 minutes")).addBranch("No")
                        .setValue("No"))

                        .addBranch(
                                "Salad",
                                new SingleFixedChoicePage(this, "Salad type").setChoices(
                                        "Greek", "Caesar").setRequired(true),

                                new SingleFixedChoicePage(this, "Dressing").setChoices(
                                        "No dressing", "Balsamic", "Oil & vinegar",
                                        "Thousand Island", "Italian").setValue("No dressing"),
                                new NumberPage(this, "How Many Salads?").setRequired(true)),
                new TextPage(this, "Comments").setRequired(true)

                        .setRequired(true),

                new CustomerInfoPage(this, "Your info").setRequired(true));
    }
}
