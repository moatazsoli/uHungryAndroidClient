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

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.DrinkItem;
import com.tech.freak.wizardpager.model.FoodItem;
import com.tech.freak.wizardpager.model.FoodOption;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.Products;
import com.tech.freak.wizardpager.model.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SandwichWizardModel extends AbstractWizardModel {
    Restaurant lTim;
    Restaurant lSubway;

    public SandwichWizardModel(Context context) {
        super(context);
        }

        @Override
    protected PageList onNewRootPageList() {
            lTim = new Restaurant("Tim Hortons");
            lTim.setProducts(new LinkedHashMap<String, ArrayList<Products>>()
                             {{
                                     put("Food", new ArrayList<Products>() {{
                                         add(new FoodItem("Bagel",
                                                         new HashMap<String, Double>(){{put("S",2.36);}},
                                                         new ArrayList<FoodOption>(){
                                                             {
                                                                 add(new FoodOption("Bread",
                                                                         FoodOption.SINGLE_CHOICE,
                                                                         new ArrayList<String>() {{
                                                                             add("Plain");
                                                                             add("Sesame");
                                                                             add("Everything");
                                                                         }})
                                                                 );
                                                                 add(new FoodOption("Veggies",
                                                                                 FoodOption.MULTI_CHOICES,
                                                                                 new ArrayList<String>() {{
                                                                                     add("Lettuce");
                                                                                     add("Tomatoes");
                                                                                 }})
                                                                 );
                                                                 add(new FoodOption("Cheese",
                                                                                 FoodOption.SINGLE_CHOICE,
                                                                                 new ArrayList<String>() {{
                                                                                     add("Cream Cheese");
                                                                                     add("Herb/Garlic");
                                                                                     add("Light");
                                                                                 }})
                                                                 );
                                                             }}


                                                 )
                                         );
                                         add(new FoodItem("Donut",
                                                         new HashMap<String, Double>(){{put("S",0.99);}},
                                                         new ArrayList<FoodOption>(){
                                                             {
                                                                 add(new FoodOption("Type",
                                                                                 FoodOption.SINGLE_CHOICE,
                                                                                 new ArrayList<String>() {{
                                                                                     add("Apple Fritter");
                                                                                     add("Chocolate Dip");
                                                                                     add("Honey Dip");
                                                                                     add("Vanilla Dip");
                                                                                     add("Maple Dip");
                                                                                     add("Chocolate Glazed");
                                                                                     add("Double Chocolate");
                                                                                     add("Old Fashion Plain");
                                                                                     add("Old Fashion Glazed");
                                                                                     add("Sour Cream Plain");
                                                                                     add("Boston Cream");
                                                                                     add("Honey Cruller");
                                                                                     add("Canadian Maple");
                                                                                 }})
                                                                 );
                                                             }}


                                                 )
                                         );
                                     }});

                                     put("Drinks", new ArrayList<Products>() {{
                                         add(new DrinkItem("Coffee",
                                                         new HashMap<String, Double>(){{put("S",1.33);put("M",1.57);put("L",1.71);put("XL",1.9);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("French Vanilla",
                                                         new HashMap<String, Double>(){{put("S",1.74);put("M",2.05);put("L",2.35);put("XL",2.59);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Iced Cap",
                                                         new HashMap<String, Double>(){{put("S",1.89);put("M",2.61);put("L",3.23);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Hot Chocolate",
                                                         new HashMap<String, Double>(){{put("S",1.43);put("M",1.67);put("L",1.92);put("XL",2.11);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Tea",
                                                         new HashMap<String, Double>(){{put("S",1.33);put("M",1.52);put("L",1.71);put("XL",1.9);}},
                                                         DrinkItem.SIGNLE_CHOICE,
                                                         new ArrayList<String>(Arrays.asList("Steeped","Green Tea","Honey Lemon","Apple Cinnamon","Blueberry White","Pomegranate White","Chamomile","Peppermint",
                                                                 "Chai Tea","Orange Pekoe","Earl Grey","English Breakfast","Decaf Orange Pekoe"))
                                                 )
                                         );
                                         add(new DrinkItem("Latte",
                                                         new HashMap<String, Double>(){{put("S",2.00);put("M",2.59);put("L",3.29);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Cappuccino",
                                                         new HashMap<String, Double>(){{put("S",2.0);put("M",2.59);put("L",3.29);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Cafe Mocha",
                                                         new HashMap<String, Double>(){{put("S",1.99);put("M",2.36);put("L",2.54);put("XL",2.86);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Iced Coffee",
                                                         new HashMap<String, Double>(){{put("S",1.48);put("M",1.81);put("L",2.13);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Lemonade",
                                                         new HashMap<String, Double>(){{put("S",1.49);put("M",2.0);put("L",2.33);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                         add(new DrinkItem("Smoothie",
                                                         new HashMap<String, Double>(){{put("S",2.69);put("M",3.49);put("L",4.29);}},
                                                         DrinkItem.NO_OPTIONS,
                                                         null
                                                 )
                                         );
                                     }});
                                 }}

            );


            lSubway = new Restaurant("Subway");
            lSubway.setProducts(new LinkedHashMap<String, ArrayList<Products>>() {{
                                    put("Food", new ArrayList<Products>() {{
                                        add(new FoodItem("Meatball Marinara",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 3.75);
                                                            put("M", 5.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Roast Beef",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 4.75);
                                                            put("M", 7.5);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Spicy Italian",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 4.75);
                                                            put("M", 7.5);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Turkey Breast",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 4.75);
                                                            put("M", 7.5);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Meatball Marinara",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 3.75);
                                                            put("M", 5.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Oven Roasted Chicken Breast",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 5.95);
                                                            put("M", 8.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Steak & Cheese",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 5.95);
                                                            put("M", 8.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Subway Club",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 5.95);
                                                            put("M", 8.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Sweet Onion Chicken Teriyaki",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 5.95);
                                                            put("M", 8.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Tuna",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 4.75);
                                                            put("M", 7.5);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );

                                        add(new FoodItem("Veggie Delite",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 3.75);
                                                            put("M", 5.95);
                                                        }},
                                                        new ArrayList<FoodOption>() {
                                                            {
                                                                add(getSubwayBread());
                                                                add(getSubwayCheese());
                                                                add(getSubwayVeggies());
                                                                add(getSubwaySauces());
                                                            }
                                                        }


                                                )
                                        );
                                    }});

                                    put("Drinks", new ArrayList<Products>() {{
                                        add(new DrinkItem("Coke",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 1.33);
                                                        }},
                                                        DrinkItem.NO_OPTIONS,
                                                        null
                                                )
                                        );
                                        add(new DrinkItem("Pepsi",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 2.00);
                                                        }},
                                                        DrinkItem.NO_OPTIONS,
                                                        null
                                                )
                                        );
                                        add(new DrinkItem("Root Beer",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 2.00);
                                                        }},
                                                        DrinkItem.NO_OPTIONS,
                                                        null
                                                )
                                        );
                                        add(new DrinkItem("Water",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 2.00);
                                                        }},
                                                        DrinkItem.NO_OPTIONS,
                                                        null
                                                )
                                        );
                                        add(new DrinkItem("Sprite",
                                                        new HashMap<String, Double>() {{
                                                            put("S", 2.00);
                                                        }},
                                                        DrinkItem.NO_OPTIONS,
                                                        null
                                                )
                                        );
                                    }});
                                }}

            );

        return new PageList(new BranchPage(this, "Resturant")
                .addRestaurant(lTim, this)
                .addRestaurant(lSubway, this)
        );
    }

    public FoodOption getSubwayVeggies()
    {
        return new FoodOption("Veggies",
                FoodOption.MULTI_CHOICES,
                new ArrayList<String>() {{
                    add("Tomatoes");
                    add("Lettuce");
                    add("Cucumbers");
                    add("Black Olives");
                    add("Green Bell Peppers");
                    add("Red Onions");
                    add("Banana Peppers");
                    add("Jalapenos");
                    add("Pickles");
                }});
    }

    public FoodOption getSubwayCheese()
    {
        return new FoodOption("Cheese",
                FoodOption.SINGLE_CHOICE,
                new ArrayList<String>() {{
                    add("American");
                    add("Monterey Cheddar");
                }});
    }

    public FoodOption getSubwayBread()
    {
        return new FoodOption("Bread",
                FoodOption.SINGLE_CHOICE,
                new ArrayList<String>() {{
                    add("9-Grain Honey Oat");
                    add("9-Grain Wheat");
                    add("Italian");
                    add("Italian Herbs & Cheese");
                    add("Flatbread (not baked in restaurant)");
                }});
    }

    public FoodOption getSubwaySauces()
    {
        return new FoodOption("Sauces",
                FoodOption.MULTI_CHOICES,
                new ArrayList<String>() {{
                    add("Chipotle Southwest");
                    add("Light or Regular Mayonnaise");
                    add("Ranch");
                    add("Oil");
                    add("Mustard");
                    add("Vinegar");
                    add("Sweet Onion");
                }});
    }
}
