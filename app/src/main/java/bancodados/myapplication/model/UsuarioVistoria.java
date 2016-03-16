package bancodados.myapplication.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by junio on 01/03/16.
 */

@DatabaseTable(tableName = "USUARIO_VISTORIA")
public class UsuarioVistoria {

    public static final String COL_ID = "ID";

    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;

    /*@ForeignCollectionField(columnName = "USUARIO_ID")
    private ForeignCollection<Usuario> usuarios;

    @ForeignCollectionField(columnName = "VISTORIA_ID")
    private ForeignCollection<Vistoria> vistorias;*/

   @DatabaseField(foreign = true, columnName = "USUARIO_ID")
    private Usuario usuario;

    @DatabaseField(foreign = true, columnName = "VISTORIA_ID")
    private Vistoria vistoria;


    @DatabaseField(columnName = "data", canBeNull = false)
    private Date data;

    public UsuarioVistoria(Date data, Vistoria vistoria,
                           Usuario usuario) {
        this.data = data;
        this.vistoria = vistoria;
        this.usuario = usuario;
    }
    public UsuarioVistoria() {}

}
