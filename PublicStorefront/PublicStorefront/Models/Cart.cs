using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PublicStorefront.Models
{
  public class CartLine
  {
    public Item Item { get; set; }
    public int Quantity { get; set; }
  }
  public class Cart
  {
    private List<CartLine> _lines = new List<CartLine>();

    public bool IsEmpty { get { return _lines.Count == 0; } }

    public IEnumerable<CartLine> Lines { get { return _lines; } }

    public void Add(Item item, int quantity) {
      _lines.Add(new CartLine { Item = item, Quantity = quantity });
    }

    public void Remove(Item item) {
      _lines.RemoveAll(x => x.Item.Id == item.Id);
    }

    public void Clear() {
      _lines.Clear();
    }
  }
}