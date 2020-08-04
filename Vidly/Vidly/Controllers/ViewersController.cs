using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Vidly.Models;
using Vidly.ViewModels;

namespace Vidly.Controllers
{
  public class ViewersController : Controller
  {
    private AppDbContext _db;

    public ViewersController()
    {
      _db = new AppDbContext();
    }
    protected override void Dispose(bool disposing)
    {
      _db.Dispose();
    }

    public ActionResult Index()
    {
      ViewBag.Message = "Your viewers description page.";

      //var customers = _db.Customers.Include("MembershipType").ToList();

      return View();
    }

    public ActionResult Details(int id)
    {
      var current = _db.Customers.Include("MembershipType").SingleOrDefault(c => c.Id == id);

      if (current == null) {
        return HttpNotFound();
      }

      return View(current);
    }

    public ActionResult AddViewer()
    {
      var membershipTypes = _db.MembershipTypes.ToList();

      var viewModel = new ViewerFormViewModel {
        Customer = new Customer(),
        MembershipTypes = membershipTypes
      };

      return View(viewModel);
    }

    public ActionResult EditViewer(int id)
    {
      var current = _db.Customers.SingleOrDefault(x => x.Id == id);

      if (current == null) {
        return HttpNotFound();
      }

      var viewModel = new ViewerFormViewModel
      {
        Customer = current,
        MembershipTypes = _db.MembershipTypes.ToList()
      };

      return View("AddViewer", viewModel);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public ActionResult SaveViewer(Customer customer)
    {
      if (!ModelState.IsValid)
      {
        var viewModel = new ViewerFormViewModel
        {
          Customer = customer,
          MembershipTypes = _db.MembershipTypes.ToList()
        };

        return View("AddViewer", viewModel);
      }

      if (customer.Id == 0)
      {
        _db.Customers.Add(customer);
      }
      else {
        var currentCustomer = _db.Customers.Single(x => x.Id == customer.Id);

        currentCustomer.Name = customer.Name;
        currentCustomer.Birthdate = customer.Birthdate;
        currentCustomer.MembershipTypeId = customer.MembershipTypeId;
        currentCustomer.IsSubscribed = customer.IsSubscribed;
      }
      _db.SaveChanges();

      return RedirectToAction("Index", "Viewers");
    }
  }
}