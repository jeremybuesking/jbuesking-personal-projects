using PublicStorefront.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace PublicStorefront.Controllers
{
  public class ShopController : Controller
  {
    private PublicStorefrontDatabase _db = new PublicStorefrontDatabase();

    // GET: Shop
    public ActionResult Index()
    {
      return View();
    }

    public ActionResult Grocery(int page = 1)
    {
      var model = GetModel("Grocery", page);
      return View(model);
    }

    public ActionResult Kitchen(int page = 1)
    {
      var model = GetModel("Kitchen", page);
      return View(model);
    }

    public ActionResult Outdoor(int page = 1)
    {
      var model = GetModel("Outdoor", page);
      return View(model);
    }

    private ItemListViewModel GetModel(string category, int page = 1, int pageSize = 4) {
      List<Item> items =
        _db.Items
           .Where(x => x.Category == category)
           .OrderBy(x => x.Name)
           .Skip((page - 1) * pageSize)
           .Take(pageSize)
           .ToList();

      int count =
        _db.Items
           .Where(x => x.Category == category)
           .Count();

      var model = new ItemListViewModel
      {
        Items = items,
        PagingInfo = new PagingInfo
        {
          CurrentPage = page,
          ItemsPerPage = pageSize,
          TotalItems = count
        }
      };

      return model;
    }
  }
}