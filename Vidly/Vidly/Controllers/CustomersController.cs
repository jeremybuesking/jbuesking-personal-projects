using System;
using System.Data.Entity;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Vidly.Models;
using Vidly.ViewModels;

namespace Vidly.Controllers
{
  public class CustomersController : Controller
  {
    private ApplicationDbContext _db;
    public CustomersController()
    {
      _db = new ApplicationDbContext();
    }
    protected override void Dispose(bool disposing)
    {
      _db.Dispose();
    }

    public ViewResult Index()
    {
      return View();
    }

    public ActionResult Details(int id)
    {
      var customer = _db.Customers.Include(x => x.MembershipType).SingleOrDefault(c => c.Id == id);

      if (customer == null)
        return HttpNotFound();

      var rentals = _db.Rentals.Include(x => x.Movie).Where(x => id.Equals(x.Customer.Id));

      var viewModel = new CustomerRentalViewModel
      { 
        Customer = customer,
        Rentals = rentals
      };

      return View(viewModel);
    }

    public ActionResult New()
    {
      var membershipTypes = _db.MembershipTypes.ToList();

      var viewModel = new CustomerFormViewModel
      {
        MembershipTypes = membershipTypes,
        Customer = new Customer()
    };

      return View("CustomerForm", viewModel);
    }

    public ActionResult Edit(int id)
    {
      var customer = _db.Customers.SingleOrDefault(x => x.Id == id);

      if (customer == null)
        return HttpNotFound();

      var viewModel = new CustomerFormViewModel
      {
        Customer = customer,
        MembershipTypes = _db.MembershipTypes.ToList()
      };

      return View("CustomerForm", viewModel);
    }

    [HttpPost]
    public ActionResult Save(Customer customer)
    {
      if (!ModelState.IsValid)
      {
        var viewModel = new CustomerFormViewModel
        {
          Customer = customer,
          MembershipTypes = _db.MembershipTypes.ToList()
        };
        return View("CustomerForm", viewModel);
      }

      if (customer.Id == 0)
        _db.Customers.Add(customer);
      else
      {
        var currentCustomer = _db.Customers.Single(x => x.Id == customer.Id);

        currentCustomer.Name = customer.Name;
        currentCustomer.Birthdate = customer.Birthdate;
        currentCustomer.MembershipTypeId = customer.MembershipTypeId;
        currentCustomer.IsSubscribedToNewsletter = customer.IsSubscribedToNewsletter;
      }
      _db.SaveChanges();

      return RedirectToAction("Index", "Customers");
    }
  }
}