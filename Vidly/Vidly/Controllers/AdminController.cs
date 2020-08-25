using System;
using Vidly.Models;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Vidly.Controllers
{
  public class AdminController : Controller
  {
    private AppDbContext _db = new AppDbContext();

    public ActionResult Index()
    {
      return View();
    }
  }
}