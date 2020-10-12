namespace Vidly.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddGenreIdAgain : DbMigration
    {
        public override void Up()
        {
          Sql("ALTER TABLE Movies ADD GenreId int");
        }
        
        public override void Down()
        {
        }
    }
}
