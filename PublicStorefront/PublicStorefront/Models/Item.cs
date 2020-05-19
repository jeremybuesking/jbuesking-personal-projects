namespace PublicStorefront.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    public partial class Item
    {
        [Key]
        public Guid Id { get; set; }

        [Required(ErrorMessage = "Please name the item.")]
        [StringLength(100, ErrorMessage = "Name is too long. Character count: 100")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Please describe the item.")]
        [StringLength(500, ErrorMessage = "Description is too long. Character count: 500")]
        public string Description { get; set; }

        [Required(ErrorMessage = "Please categorize the item.")]
        [StringLength(50, ErrorMessage = "Category name is too long. Character count: 50")]
        public string Category { get; set; }

        //FIZME: Make NOT NULL
        public decimal Price { get; set; }
    }
}
