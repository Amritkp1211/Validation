const search=()=>{
 //   console.log("searching.......");
  
    let query= $("#search-input").val();
    console.log(query);

    if(query==""){
        $(".search-result").hide();
    }else{

        //search
        //console.log(query);
      //sending request to server
          let url=`http://localhost:8888/search/${query}`;
        
          fetch(url).then((response)=>{
			  console.log(response);
            return response.json();
         
            }).then((data)=>{
              //data.....
             // console.log(data);
                   console.log(data);
              let text=`<div class='list-group'>`
            
          
              data.forEach(contact => {
				  
                text+=`<a href='/user/contact/${contact.cid}' class='list-group-item list-group-item-action'>${contact.name}</a>`;
              });  

              text+=`</div>`;

              $(".search-result").html(text);
              $(".search-result").show();
            }).catch(error => {
				console.log(error.message)
             console.error('Fetch error:', error);
          // Handle the error gracefully
         });
      
    }

}
