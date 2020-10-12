import React from "react";

import classes from "./Burger.module.css";
import BurgerIngredient from "./BurgerIngredient/BurgerIngredient";

const burger = (props) => {
  let transformedIngredients = null;

  transformedIngredients = Object.keys(props.ingredients)
    .map((ingKey) => {
      //console.log(props.ingredients + "<= ingredients as props");
      return [...Array(props.ingredients[ingKey])].map((_, i) => {
        console.log();
        return <BurgerIngredient key={ingKey + i} type={ingKey} />;
      });
    })
    .reduce((arr, el) => {
      return arr.concat(el);
    }, []);
  console.log(transformedIngredients);

  if (transformedIngredients.length === 0) {
    transformedIngredients = <p>Start adding ingredients!</p>;
  }

  return (
    <div className={classes.Burger}>
      <BurgerIngredient type="bread-top" />
      {transformedIngredients}
      <BurgerIngredient type="bread-bottom" />
    </div>
  );
};

export default burger;
