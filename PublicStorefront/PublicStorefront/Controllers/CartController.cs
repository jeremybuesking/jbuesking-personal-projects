using PublicStorefront.Models;
using PublicStorefront.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace PublicStorefront.Controllers
{
  public class CartController : Controller
  {
    private PublicStorefrontDatabase _db = new PublicStorefrontDatabase();

    private Cart GetCart() {
      Cart cart = Session["Cart"] as Cart;

      if (cart == null) {
        cart = new Cart();
        Session["Cart"] = cart;
      }

      return cart;
    }
    public ActionResult Index(Cart cart, string returnUrl)
    {
      ViewBag.returnUrl = returnUrl;

      return View(new CartIndexViewModel
      {
        Cart = cart,
        ReturnUrl = returnUrl
      });
    }

    [HttpPost]
    public ActionResult AddToCart(Cart cart, Guid itemId, string returnUrl) 
    {
      Item item = _db.Items.SingleOrDefault(x => x.Id == itemId);
      if (item != null) {
        cart.Add(item, 1);
      }

      return RedirectToAction("Index", routeValues: new { returnUrl });
    }

    [HttpPost]
    public ActionResult RemoveFromCart(Cart cart, Guid itemId, string returnUrl)
    {
      Item item = _db.Items.SingleOrDefault(x => x.Id == itemId);
      if (item != null)
      {
        cart.Remove(item);
      }

      return RedirectToAction("Index", routeValues: new { returnUrl });
    }

    public PartialViewResult Summary(Cart cart)
    {
      return PartialView(cart);
    }

    [HttpGet]
    public ActionResult Checkout(Cart cart) {
      if (cart.IsEmpty)
      {
        ModelState.AddModelError("", "Your cart is empty!");
      }

      return View();
    }

    [HttpPost]
    public ActionResult Checkout(Cart cart, ShippingDetails shippingDetails)
    {
      if (cart.IsEmpty) {
        ModelState.AddModelError("", "Your cart is empty!");
      }

      if (!ModelState.IsValid) {
        return View(shippingDetails);
      }

      return RedirectToAction("Completed");
    }

    public ActionResult Completed() {
      return View();
    }
  }
}