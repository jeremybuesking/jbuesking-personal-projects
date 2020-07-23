using PublicStorefront;
using PublicStorefront.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity; // Needed for ToListAsync and CountAsync
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace PublicStorefront.Controllers
{
    public class HomeController : Controller
    {
        private PublicStorefrontDatabase _db = new PublicStorefrontDatabase();
        public async Task<ActionResult> Index(int page = 1, int pageSize = 4)
        {
            List<Item> items =
                await _db.Items
                         .OrderBy(x => x.Name)
                         .Skip((page - 1) * pageSize)
                         .Take(pageSize)
                         .ToListAsync();

            int count =
                await _db.Items.CountAsync();

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