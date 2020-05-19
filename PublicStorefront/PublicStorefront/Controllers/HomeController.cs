using PublicStorefront;
using PublicStorefront.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace PublicStorefront.Controllers
{
    public class HomeController : Controller
    {
        private PublicStorefrontDatabase _db = new PublicStorefrontDatabase();
        public ActionResult Index(int page = 1, int pageSize = 4)
        {
            List<Item> items =
                _db.Items
                    .OrderBy(x => x.Name)
                    .Skip((page - 1) * pageSize)
                    .Take(pageSize)
                    .ToList();

            var model = new ItemListViewModel
            {
                Items = items,
                PagingInfo = new PagingInfo
                {
                    CurrentPage = page,
                    ItemsPerPage = pageSize,
                    TotalItems = _db.Items.Count()
                }
            };

            return View(model);
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}