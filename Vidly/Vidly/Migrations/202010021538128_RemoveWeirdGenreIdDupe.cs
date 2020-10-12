namespace Vidly.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class RemoveWeirdGenreIdDupe : DbMigration
    {
        public override void Up()
        {
          Sql("ALTER TABLE Movies DROP COLUMN GenreId");
        }
        
        public override void Down()
        {
        }
    }
}
