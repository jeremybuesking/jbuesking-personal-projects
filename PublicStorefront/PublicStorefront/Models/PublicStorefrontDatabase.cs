namespace PublicStorefront.Models
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;

    public partial class PublicStorefrontDatabase : DbContext
    {
        public PublicStorefrontDatabase()
            : base("name=PublicStorefrontDatabase")
        {
        }

        public virtual DbSet<Item> Items { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Item>()
                .Property(e => e.Price)
                .HasPrecision(16, 2);
        }
    }
}
